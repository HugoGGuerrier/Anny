// --- Code import
import React from 'react';
import Ajax from "../tools/Ajax";
import Error from "./Error";

// --- Resources import
import logo from '../../img/logo.png';
import '../../css/Welcome.css';

/**
 * This class is the first screen of the application it will test if the user is logged in and offer him to register
 */
class Welcome extends React.Component {

    // ----- Constructor -----


    /**
     * Default constructor
     *
     * @param props The component props
     */
    constructor(props) {
        super(props);

        this.state = {
            loggedIn: null,
            error: null
        };

        // Test if the user is logged in
        Ajax.sendRequest("GET", "login", (r) => {this.handleLoginTest(r)}, null);
    }


    // ----- Class methods -----


    /**
     * Handle the login test response
     *
     * @param response The JSON response from the server
     */
    handleLoginTest(response) {
        if(response.hasOwnProperty("result")) {
            switch (response.result) {
                case "SUCCESS":
                    this.setState({loggedIn: true});
                    break;
                case "NOT_LOGGED":
                    this.setState({loggedIn: false});
                    break;
                default:
                    this.setState({error: response});
                    break;
            }
        }
    }


    // ----- Render method -----


    /**
     * Method to render the component
     *
     * @returns {*} The JSX render
     */
    render() {
        if(this.state.loggedIn !== null) {

            if(this.state.loggedIn) {

                // Redirect to the home component
                return (
                    <div className="App">
                        <div className="App-header">
                            <img src={logo} className="App-logo" alt="logo" />
                            <h2>Welcome to Anny (Not developed yet...)</h2>
                        </div>
                        <p className="App-intro">
                            You'll have to wait fot the developer to work on it...
                        </p>
                    </div>
                );

            } else {

                return (
                    <div className="App">
                        <div className="App-header">
                            <img src={logo} className="App-logo" alt="logo" />
                            <h2>Welcome to Anny (Not developed yet...)</h2>
                        </div>
                        <p className="App-intro">
                            You'll have to wait fot the developer to work on it...
                        </p>
                    </div>
                );

            }

        } else if(this.state.error !== null) {

            // Render the error template
            return (
                <Error error={this.state.error}/>
            );

        } else {

            // Until the server answer, display nothing
            return null;

        }

    }

}

export default Welcome;
