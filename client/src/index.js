import React from 'react';
import ReactDOM from 'react-dom';
import Welcome from './js/components/Welcome';
import './css/index.css';
import Config from "./js/tools/Config";

// Initialize the application configuration
Config.initConfig();

console.log(Config.apiUrl);

ReactDOM.render(
  <Welcome />,
  document.getElementById('root')
);