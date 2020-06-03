/**
 * This class contain the application configuration
 */
class StdVar {

    // ----- Attributes -----


    static env = 0;
    static apiUrl;

    /** Api response possibility */
    static successResponse = "SUCCESS";
    static notLoggedResponse = "NOT_LOGGED";
    static failResponse = "FAIL";


    // ----- Class method -----


    static initConfig() {
        if(StdVar.env === 0) {

            StdVar.apiUrl = "http://localhost:8080/anny/";

        } else {

            StdVar.apiUrl = window.location.protocol + "//" + window.location.host + "/" + process.env.PUBLIC_URL + "/";

        }
    }

}

export default StdVar;