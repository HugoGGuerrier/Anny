// --- Resources import
import logo from '../../img/logo.png';
import '../../css/Welcome.css';

// --- Code import
import React from 'react';
import Ajax from "../tools/Ajax";
import Error from "./Error";
import {Redirect} from "react-router-dom";
import AppRouter from "./AppRouter";
import StdVar from "../tools/StdVar";


/**
 * This class represent the login form
 */
class Login extends React.Component {

}

/**
 * This class represent the register form
 */
class Register extends React.Component {

}

/**
 * This class is the component where the user choose between login and register
 */
class Welcome extends React.Component {

    // ----- Attributes -----


    showArray = {welcomeText: true, loginForm: false, registerForm: false};


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
                case StdVar.successResponse:
                    this.setState({loggedIn: true});
                    break;
                case StdVar.notLoggedResponse:
                    this.setState({loggedIn: false});
                    break;
                default:
                    this.setState({error: response});
                    break;
            }
        }
    }

    /**
     * Hide the welcome text
     */
    hideWelcomeText() {
        if(this.showArray.welcomeText) {
            this.hideElement(document.getElementById("welcome-text"));
            this.showArray.welcomeText = false;
        }
    }

    /**
     * Show the login form
     */
    showLoginForm() {
        if(!this.showArray.loginForm) {
            this.showElement(document.getElementById("login-form"));
            this.hideElement(document.getElementById("register-form"));
            this.showArray.loginForm = true;
            this.showArray.registerForm = false;
        }
    }

    /**
     * Show the register form
     */
    showRegisterForm() {
        if(!this.showArray.registerForm) {
            this.showElement(document.getElementById("register-form"));
            this.hideElement(document.getElementById("login-form"));
            this.showArray.registerForm = true;
            this.showArray.loginForm = false;
        }
    }

    /**
     * Show an html element
     *
     * @param htmlElement
     */
    showElement(htmlElement) {
        htmlElement.style.zIndex = "1";

        window.setTimeout(function () {
            htmlElement.style.opacity = "1";
        }, 10);
    }

    /**
     * Hide an html element
     *
     * @param htmlElement
     */
    hideElement(htmlElement) {
        htmlElement.style.opacity = "0";

        window.setTimeout(function () {
            htmlElement.style.zIndex = "-1";
        }, 210);
    }


    // ----- Render method -----


    /**
     * The render function
     *
     * @returns {null|*}
     */
    render() {
        if(this.state.loggedIn !== null) {

            if(this.state.loggedIn) {

                // Send the user to the home
                return (
                    <Redirect to={AppRouter.homeRoute} />
                );

            } else {

                // Return the welcome page
                return (
                    <div id={"welcome-root"}>

                        <div id={"side-container"}>
                            <div id={"link-container"}>
                                <a id={"login-button"} className={"side-button"} onClick={() => {this.hideWelcomeText(); this.showLoginForm();}}>LOGIN</a>
                                <a id={"register-button"} className={"side-button"} onClick={() => {this.hideWelcomeText(); this.showRegisterForm();}}>REGISTER</a>
                            </div>
                        </div>

                        <section id={"welcome-section"}>
                            <div id={"welcome-container"}>
                                <div id={"welcome-text"}>
                                    <img src={logo} alt={"Anny's logo"} />
                                    <h1>Welcome to Anny !</h1>
                                    <p>Anny is a simple and open source social network based on the anonymity.</p>
                                    <p>Everyone can create an account, post messages and be a part of the community.</p>
                                    <p>We hope you'll have a great time.</p>
                                </div>

                                <form id={"login-form"}>
                                    <h1>Login</h1>
                                    <input id={"login-id-input"} className={"form-input"} type={"text"} placeholder={"@user_id"} />
                                    <input id={"login-password-input"} className={"form-input"} type={"password"} placeholder={"Password"} />
                                    <input id={"login-submit"} className={"form-submit-button"} type={"button"} value={"Login"} />
                                </form>

                                <form id={"register-form"}>
                                    <h1>Register</h1>
                                    <input id={"register-mail-input"} className={"form-input"} type={"text"} placeholder={"Email address"} />
                                    <input id={"register-pseudo-input"} className={"form-input"} type={"text"} placeholder={"User pseudo"} />
                                    <input id={"register-id-input"} className={"form-input"} type={"text"} placeholder={"User ID"} />
                                    <input id={"register-password-input"} className={"form-input"} type={"password"} placeholder={"Password"} />
                                    <input id={"register-submit"} className={"form-submit-button"} type={"button"} value={"Register"} />
                                </form>
                            </div>
                        </section>

                    </div>
                );

            }

        } else if(this.state.error !== null) {

            // Render the error template
            return (
                <Error error={this.state.error} />
            );

        } else {

            // Until the server answer, display nothing
            return null;

        }

    }

}

export default Welcome;
