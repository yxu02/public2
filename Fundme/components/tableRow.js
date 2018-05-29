import React, {Component} from 'react';
import { Table, Button } from 'semantic-ui-react';
import web3 from '../ethereum/web3';
import Campaign from '../ethereum/campaign';

class TableRow extends Component{
  
  state = {
    loadingApproveRequest: false,
    loadingFinalizeRequest: false,
    errorMessage: ''
  };
  
  onApprove = async()=>{
    this.setState({loadingApproveRequest:true, errorMessage:''});
    try{
      const accounts = await web3.eth.getAccounts();
      const campaign = Campaign(this.props.address);

      await campaign.methods.approveRequest(this.props.id).send({
        from: accounts[0]
      });
    } catch (err){
      this.setState({errorMessage: err.message});
    }
    
    this.setState({loadingApproveRequest:false});
  };
  
  onFinalize = async()=>{
    
    this.setState({loadingFinalizeRequest:true, errorMessage:''});
    try{
    const accounts = await web3.eth.getAccounts();
    const campaign = Campaign(this.props.address);
    
    await campaign.methods.finalizeRequest(this.props.id).send({
      from: accounts[0]
    });
    } catch(err){
      this.setState({errorMessage: err.message});      
    }
    
    this.setState({loadingFinalizeRequest:false});
  };
  
  render(){
    const {Row, Cell}=Table;
    const {description, value, recipient, approvalCount, complete} = this.props.request;
    const readyToFinalize = approvalCount > this.props.approverCount/2;
    return (
      <Row disabled={complete} positive={readyToFinalize && !complete} error={!!this.state.errorMessage}>
        <Cell>{this.props.id+1}</Cell>
        <Cell>{description}</Cell>
        <Cell>{web3.utils.fromWei(value, 'ether')}</Cell>
        <Cell>{recipient}</Cell>
        <Cell>{approvalCount}/{this.props.approverCount}</Cell>
        <Cell>
          { (complete || readyToFinalize)? null:(
          <Button loading={this.state.loadingApproveRequest} primary onClick={this.onApprove}>Approve</Button>
          )}   
        </Cell>
        <Cell>
          { complete ? null:(
          <Button loading={this.state.loadingFinalizeRequest} primary onClick={this.onFinalize}>Finalize</Button>
          )}     
        </Cell>
      </Row>
    );
  }
}

export default TableRow;