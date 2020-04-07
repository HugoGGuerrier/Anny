// --- Code import
import React from 'react'

// --- Resources import
import "../../css/Error.css"

class Error extends React.Component {

    // ----- Constructor -----


    /**
     * Construct a new error page with the props of the error you want to display
     *
     * @param props
     */
    constructor(props) {
        super(props);

        this.errorCodeSignification = {
            "-1": "WEB ERROR",
            "100": "JSON ERROR",
            "500": "SERVER ERROR",
            "1000": "SQL ERROR",
            "10000": "MONGO ERROR",
            "100000": "JAVA ERROR"
        };
    }


    // ----- Lifecycle methods -----


    /**
     * Method called just when the component is put in the DOM
     */
    componentDidMount() {
        document.body.style.backgroundColor = "black";
        document.body.style.fontFamily = "sans-serif";
    }

    /**
     * Method called when the component will be unmount
     */
    componentWillUnmount() {
        document.body.style.backgroundColor = "white";
        document.body.style.fontFamily = "Montserrat";
    }


    // ----- Render method -----


    /**
     * Render teh error page
     *
     * @returns {*} The JSX of error page
     */
    render() {
        let errorCode = this.props.error.errorCode;
        let errorMessage = this.props.error.errorMessage;

        if(this.props.error.hasOwnProperty("errorStackTrace")) {
            console.log(this.props.error);
        }

        return (
            <div id="error-container">
                <p>Anny > Error during the application execution :(</p>
                <p>Anny > Error code : {errorCode} ({this.errorCodeSignification[errorCode]})</p>
                <p>Anny > Error message : {errorMessage}</p>
                <p>Anny > <em id="cursor">|</em></p>
            </div>
        );
    }

}

export default Error;