const assert = require('assert');
const ganache = require('ganache-cli');
const Web3 = require('web3');
const web3 = new Web3(ganache.provider());

const compiledFactory = require('../ethereum/build/CampaignFactory.json');
const compiledCampaign = require('../ethereum/build/Campaign.json');

let accounts;
let factory;
let campaign;
let campaignAddress;

beforeEach(async ()=>{
  
  accounts = await web3.eth.getAccounts();
  factory = await new web3.eth.Contract(JSON.parse(compiledFactory.interface))
    .deploy({ data: compiledFactory.bytecode })
    .send({ from: accounts[0], gas: '1000000' });
  await factory.methods.createCampaign('100').send({
    from: accounts[0], gas:'1000000'
  });
  
  [campaignAddress] = await factory.methods.getDeployedCampaigns().call();
  
  campaign = await new web3.eth.Contract(JSON.parse(compiledCampaign.interface), campaignAddress);
});

describe('Campaign', ()=>{
  
  it('deployed campaign & factory', ()=>{
    assert.ok(campaign.options.address);
    assert.ok(factory.options.address);
  });
  
  it('is manager', async()=>{
    const manager = await campaign.methods.manager().call();
    assert.equal(accounts[0], manager);
  });
  
  it('contributor is approver', async()=>{
    await campaign.methods.contribute().send({
      from: accounts[1], gas:'1000000', value:'1000'
    });
    const isApprover = await campaign.methods.approvers(accounts[1]).call();
    assert(isApprover);
  });
  
  it('is minimum donation', async()=>{
    try{
      await campaign.methods.contribute().send({
        from: accounts[2], gas:'1000000', value:'50'
      });
      assert(false);
    } catch(err){
      assert(err);
    }
  });
  
//  it('request value cannot exeed balance', async()=>{
//    try{
//      await campaign.methods.createRequest('buy stuff', '1001', accounts[2]).send({ from: accounts[0], gas:'1000000' });
//      assert(false);
//    } catch(err){
//      assert(err);
//    }
//  });
  
  it('request created by manager', async()=>{
    await campaign.methods.createRequest('buy stuff', '100', accounts[2]).send({ from: accounts[0], gas:'1000000' });
    
    const request = await campaign.methods.requests(0).call();
    assert.equal('buy stuff', request.description);
    assert.equal(0, request.approvalCount);
    assert.equal(accounts[2], request.recipient);
    assert.equal(100, request.value);
  });
  
  it('complete whole process', async()=>{
    await campaign.methods.contribute().send({
      from: accounts[3], gas:'1000000', value: web3.utils.toWei('10', 'ether')
    });
    
    await campaign.methods.createRequest('buy stuff', web3.utils.toWei('5', 'ether'), accounts[4]).send({ from: accounts[0], gas:'1000000' });
    
    await campaign.methods.approveRequest(0).send({ 
      from: accounts[3], gas:'1000000'
    });
//    const approvedRequest = await campaign.methods.requests(0).call();
//    assert(request.approvals(accounts[1]));
    await campaign.methods.finalizeRequest(0).send({
      from: accounts[0], gas:'1000000'
    });
    
    let balance = await web3.eth.getBalance(accounts[4]);
    balance = web3.utils.fromWei(balance, 'ether');
    balance = parseFloat(balance);
    console.log(balance);
    assert(balance>103);
  });
});
