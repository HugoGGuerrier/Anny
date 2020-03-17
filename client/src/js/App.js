import React from 'react';
import logo from '../img/logo.png';
import '../css/App.css';

class App extends React.Component {
    render() {
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
}

export default App;
