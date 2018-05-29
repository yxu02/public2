import React, {Component} from 'react';
import Layout from '../../../components/layout';
import Campaign from '../../../ethereum/campaign';
import { Button, Table } from 'semantic-ui-react';
import { Link } from '../../../routes';
import TableRow from '../../../components/tableRow';

class Requests extends Component{
  
  static async getInitialProps(props){
    const campaign = Campaign(props.query.address);
    const reqCount = await campaign.methods.getRequestsCount().call();
    const approverCount = await campaign.methods.approverCount().call();
    
    const requests = await Promise.all(
      Array(parseInt(reqCount)).fill()
      .map((element, index)=>{
        return campaign.methods.requests(index).call();
      })
    );
    return {
      address: props.query.address,
      requests: requests,
      approverCount: approverCount,
      reqCount: reqCount
    }; 
  }
  
  renderRows(){
    return this.props.requests.map((request, index)=>{
      return (
        <TableRow 
          key = {index}
          id = {index}
          approverCount = {this.props.approverCount}
          request = {request}
          address = {this.props.address}
        />
      );
    });
  }
  render(){
    const {Header, Row, HeaderCell, Body} = Table;
    return (
      <Layout>
        <h3>Requests</h3>
        <Link route={`/campaigns/${this.props.address}/requests/new/`}>
            <a>
            <Button floated = "right" primary style={{marginBottom:10}}>New Request</Button>
            </a>
        </Link>
        <Table>
          <Header>
            <Row>
              <HeaderCell>ID</HeaderCell>
              <HeaderCell>Description</HeaderCell>
              <HeaderCell>Amount</HeaderCell>
              <HeaderCell>Recipient</HeaderCell>
              <HeaderCell>Approval Count</HeaderCell>
              <HeaderCell>Approve</HeaderCell>
              <HeaderCell>Finalize</HeaderCell>
            </Row>
          </Header>
          <Body>
            {this.renderRows()}
          </Body>
        </Table>
        <div>Found {this.props.reqCount} Requests </div>
      </Layout>
    );
  }
}

export default Requests;