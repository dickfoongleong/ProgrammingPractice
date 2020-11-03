import React, { Component } from 'react';
import axios from 'axios';
class Myform extends Component {
    constructor() {

      super();
      this.state = {
        description : ''
    }
      this.handleSubmit = this.handleSubmit.bind(this);
    }

    handleSubmit(event) {
      event.preventDefault(); //Avoid page refresh
      var doggy = {
        fname : this.state.description,
        lname : "lname",
        email: this.state.description,
        password: "pwd"

    }
    console.log(doggy);

     axios.post('http://www.lafarleaf.com/users/',doggy , 
     {header : {'Content-Type' : 'application/json' }})
        .then(response => {
            console.log("sent successfully")
        })




    }

    render() {
      return (
        <form onSubmit={this.handleSubmit}>
          <label htmlFor="username">Enter username</label>
          <input id="username" name="username" type="text"  onChange={(e) => this.setState({description: e.target.value})}/>

          <button onClick={() => this.handleSubmit}> Send data! </button>
        </form>
      );
    }
  }
  export default Myform;
