import React, {Component} from 'react';
import { Card, Grid, Button } from 'semantic-ui-react';
import Layout from '../../components/layout';
import Campaign from '../../ethereum/campaign';
import web3 from '../../ethereum/web3';
import ContributionForm from '../../components/contributionForm';
import { Link } from '../../routes';

class CampaignShow extends Component{
  //let js code executed at server side and render final html on browser.
  static async getInitialProps(props){
    const campaign = Campaign(props.query.address);
    const summary = await campaign.methods.getSummary().call();
    return {
      address: props.query.address,
      minimumContribution: summary[0],
      balance: summary[1],
      requestsCount: summary[2],
      approversCount: summary[3],
      manager: summary[4]
    }; 
  }
  
  renderCampaign(){
    //mapping to iterate props.campaign for card views
    const items = [
      {
        header: this.props.manager,
        meta: 'Address of campaign organizer',
        description: 'Campaign organizer creates campaign and raise fund.',
        style: { overflowWrap: 'break-word'}
      },{
        header: this.props.minimumContribution,
        meta: 'Minimum contribution (unit: wei)',
        description: 'Minimum contribution needed to join the campaign.',
        style: { overflowWrap: 'break-word'}
      },{
        header: this.props.requestsCount,
        meta: 'Number of requests',
        description: 'A request is sent by campaign organizer to approvers (campaign contributors) for appropriate use of the campaign funds',
        style: { overflowWrap: 'break-word'}        
      },{
        header: this.props.approversCount,
        meta: 'Number of approvers',
        description: 'A approver who has contributed to the campaign is eligible to vote for or against requests.',
        style: { overflowWrap: 'break-word'}              
      },{
        header: web3.utils.fromWei(this.props.balance,'ether'),
        meta: 'Campaign balance (unit: ether)',
        description: 'The balance in the current campaign.',
        style: { overflowWrap: 'break-word'}  
      }
    ];
    
    return <Card.Group items={items} />
  }
  
  render(){
    return (
      <Layout>
        <h3>Campaign Profile</h3>
        <Grid>
          <Grid.Row>
      
            <Grid.Column width={10}>
              {this.renderCampaign()}
            </Grid.Column>
            
            <Grid.Column width={6}>
              <ContributionForm address={this.props.address}/>
            </Grid.Column>
              
          </Grid.Row>
          <Grid.Row>
            <Grid.Column>
              <Link route={`/campaigns/${this.props.address}/requests`}>
                <a>
                  <Button primary>View Requests</Button>
                </a>
              </Link>
            </Grid.Column>
          </Grid.Row>
        </Grid>
      </Layout>
    );
  }
}

export default CampaignShow;