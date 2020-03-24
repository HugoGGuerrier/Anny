class Config {

    // ----- Attributes -----


    // ----- Class methods -----


    static initConfig() {
        if(typeof baseConfig !== 'undefined') {

            // Set the app config
            console.log("DEFINED !");

        } else {

            // Define the dev config
            console.log("UNDEFINED !");

        }
    }

}

export default Config;