// --- Code import
import React from "react";
import {
    BrowserRouter as Router,
    Switch,
    Route
} from "react-router-dom";
import Welcome from "./Welcome";
import Home from "./Home";


/**
 * This class the the first component loaded, it set the router on the application. This is a static component, loaded
 * once when you request the application.
 */
class AppRouter extends React.Component {

    // ----- Attributes -----


    /** All application router */
    static registerRoute = "/register";
    static loginRoute = "/login";
    static homeRoute = "/home";
    static defaultRoute = "/";


    // ----- Render method -----


    render() {
        return(
            <Router>
                <Switch>

                    <Route path={AppRouter.homeRoute} component={Home} />

                    {/* Default route */}
                    <Route path={AppRouter.defaultRoute} component={Welcome} />

                </Switch>
            </Router>
        );
    }

}

export default AppRouter;