import React, {Component} from 'react';
import { Card, Button } from 'semantic-ui-react';
//import 'semantic-ui-css/sematic.min.css';
import factory from '../ethereum/factory';
import Layout from '../components/layout';
import { Link } from '../routes';

class CampaignIndex extends Component{
  //let js code executed at server side and render final html on browser.
  static async getInitialProps(){
    const campaigns = await factory.methods.getDeployedCampaigns().call();
    
    return { campaigns };
  }
  
  renderCampaigns(){
    //mapping to iterate props.campaign for card views.
    const items = this.props.campaigns.map(address =>{
      return {
        header: address,
        description: (
          <Link route={`/campaigns/${address}/`}>
          <a>View Campaign</a>
          </Link>
          ),
        fluid: true
      };
    });
    
    return <Card.Group items={items} />
  }

  render() {
    return (
      <Layout>
        <div>
          
          <h3>Open Campaigns</h3>
          <Link route="/campaigns/new">
            <a>
            <Button
              floated = "right"
              content = "Create Campaign"
              icon = "add"
              primary
            />
            </a>
          </Link>
          {this.renderCampaigns()}
        </div>
      </Layout>
    );
  }
}

export default CampaignIndex;