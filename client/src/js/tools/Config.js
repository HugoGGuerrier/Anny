/**
 * This class contain the application configuration
 */
class Config {

    // ----- Attributes -----


    static env = 0;
    static apiUrl;

    /** Api response possibility */
    static successResponse = "SUCCESS";
    static notLoggedResponse = "NOT_LOGGED";
    static failResponse = "FAIL";


    // ----- Class method -----


    static initConfig() {
        if(Config.env === 0) {

            Config.apiUrl = "http://localhost:8080/anny/";

        } else {

            Config.apiUrl = window.location.protocol + "//" + window.location.host + "/" + process.env.PUBLIC_URL + "/";

        }
    }

}

export default Config;