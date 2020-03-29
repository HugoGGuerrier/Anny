// --- Code import
import React from 'react';
import Ajax from "../tools/Ajax";
import Home from "./Home";
import Error from "./Error";

// --- Resources import
import logo from '../../img/logo.png';
import '../../css/Welcome.css';

/**
 * This class is the component where the user choose between login and register
 */
class Welcome extends React.Component {

    // ----- Constructor -----


    /**
     * Construct a new component and send a request to the server
     *
     * @param props
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


    render() {
        if(this.state.loggedIn !== null) {

            if(this.state.loggedIn) {

                // Send the user to the home
                return (
                    <Home />
                );

            } else {

                return (
                    <p>Welcome page</p>
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
