import Config from "./Config";

class Ajax {

    // ----- Class methods -----


    /**
     * Send an Ajax request to the server and call a function after it
     *
     * @param method
     * @param point
     * @param callback
     * @param data
     */
    static sendRequest(method, point, callback, data = null) {
        // Create the HTTP request
        let connection = new XMLHttpRequest();

        // Create all the HTTP request params
        let url = Config.apiUrl + point;
        let body = null;

        // Format the data to the correct format
        if(data !== null) {
            if(method === "POST") {
                body = Ajax.formatArgs(data);
            } else {
                url += "?" + Ajax.formatArgs(data);
                console.log(url);
            }
        }

        // Open the connection and send the request
        connection.open(method, url, true);
        connection.send(body);

        // Wait for the request answer
        connection.onreadystatechange = function (e) {
            if(connection.readyState === 4) {
                if (callback !== null) {
                    // Prepare the response JSON
                    let responseJson = {};

                    if(connection.status >= 200 && connection.status <= 226) {
                        try {
                            responseJson = JSON.parse(connection.responseText);
                        } catch (e) {
                            responseJson = {
                                result: "FAIL",
                                errorCode: 100,
                                errorMessage: "Cannot parse the JSON string : " + connection.responseText
                            }
                        }
                    } else {
                        responseJson = {
                            result: "FAIL",
                            errorCode: 500,
                            errorMessage: "Cannot reach the server, it must be down... Please wait until it works again (or repair it if you're a developer)",
                        };
                    }
                    callback(responseJson);
                }
            }
        };
    }

    /**
     * Format the data for a get like request
     *
     * @param data The data object to format
     * @returns {string} The String of the args
     */
    static formatArgs(data) {
        let res = "";
        for(let argName in data) {
            if(data.hasOwnProperty(argName)) {
                res += argName + "=" + data[argName] + "&";
            }
        }
        return res.slice(0, res.length - 1);
    }

}

export default Ajax;