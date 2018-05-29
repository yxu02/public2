pragma solidity ^0.4.17;

contract CampaignFactory{
    address[] deployedCampaigns;
    
    function createCampaign(uint minimum) public {
        address newCampaign = new Campaign(minimum, msg.sender);
        deployedCampaigns.push(newCampaign);
    }
    
    function getDeployedCampaigns() public view returns (address[]){
        return deployedCampaigns;
    }
}

contract Campaign{
    struct Request{
        string description;
        uint value;
        address recipient;
        bool complete;
        uint approvalCount;
        mapping(address=>bool) approvals;
    }
    
    address public manager;
    mapping(address=>bool) public approvers;
    uint public approverCount;
    uint public minimumContribution;
    Request[] public requests;
    
    constructor(uint minimum, address creator) public {
        manager = creator;
        minimumContribution = minimum;
    }
    
    function contribute() public payable{
        require(msg.value > minimumContribution);
        
        approvers[msg.sender] = true;
        approverCount++;
    }
    
    function createRequest(string desc, uint value, address recipient) public {
        require(msg.sender == manager);
        
        Request memory newRequest = Request({
            description: desc,
            value: value,
            recipient: recipient,
            complete: false,
            approvalCount: 0
        });
        
        requests.push(newRequest);
    }
    
    function approveRequest(uint index) public {
        Request storage request = requests[index]; 
        
        require(approvers[msg.sender]);
        require(!request.approvals[msg.sender]);
        
        request.approvals[msg.sender] = true;
        request.approvalCount++;
    }
    
    function finalizeRequest(uint index) public{
        require(msg.sender == manager);
        Request storage request = requests[index];
        require(!request.complete);
        require(request.approvalCount > approverCount/2);
        
        request.recipient.transfer(request.value);
        request.complete = true;
    }
    
    function getSummary() public view returns(uint, uint, uint, uint, address){
        return (
            minimumContribution,
            address(this).balance,
            requests.length,
            approverCount,
            manager
        );
    }
    
    function getRequestsCount() public view returns(uint){
        return requests.length;
    }
}