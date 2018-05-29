import React, { Component } from 'react';
import { Form, Button, Input, Message } from 'semantic-ui-react';
import Layout from '../../../components/layout';
import Campaign from '../../../ethereum/campaign';
import web3 from '../../../ethereum/web3';
import { Link, Router } from '../../../routes';

class RequestNew extends Component {
  static async getInitialProps(props){
    return {
      address: props.query.address
    }; 
  }
  
  state = {
    desc: '',
    value: '',
    rec: '',
    errorMessage: '',
    loading: false
  };

  onSubmit = async (event)=>{
    event.preventDefault();
    this.setState({loading:true, errorMessage:''});
    const campaign = Campaign(this.props.address);
    try{
      const accounts = await web3.eth.getAccounts();
      await campaign.methods
        .createRequest(this.state.desc, web3.utils.toWei(this.state.value,'ether'), this.state.rec)
        .send({
          from: accounts[0] 
        });
      Router.pushRoute(`/campaigns/${this.props.address}/requests`);
    } catch(err){
      this.setState({errorMessage: err.message});
    }
    this.setState({loading:false});
  };
  render (){
    return (
      <Layout>
        <Link route={`/campaigns/${this.props.address}/requests`}>
          <a>Back</a>
        </Link>
        <h3>Create a Request</h3>
        <Form onSubmit={this.onSubmit} error={!!this.state.errorMessage}>
          <Form.Field>
            <label>Description</label>
            <Input placeholder = "Request description"
              value = {this.state.desc}
              onChange = {(event)=>{
                this.setState({desc: event.target.value})
              }}
            />
          </Form.Field>
          <Form.Field>
            <label>Request Expense in Ether</label>
            <Input placeholder = "$$$ expense in ether"
              value = {this.state.value}
              onChange = {(event)=>{
                this.setState({value: event.target.value})
              }}
            />
          </Form.Field>
          <Form.Field>
            <label>Recipient Address</label>
            <Input placeholder = "Request recipient address"
              value = {this.state.rec}
              onChange = {(event)=>{
                this.setState({rec: event.target.value})
              }}
            />
          </Form.Field>
          <Message
            error
            header="Error occurred!"
            content={this.state.errorMessage}/>
          <Button loading={this.state.loading} primary type='submit'>Create</Button>

        </Form>
      </Layout>
    );
  }
}

export default RequestNew;