import React from 'react';
import ReactDOM from 'react-dom';
import Welcome from './js/components/Welcome';
import Config from "./js/tools/Config";
import './css/index.css';

// Initialize the application configuration
Config.initConfig();

ReactDOM.render(
  <Welcome />,
  document.getElementById('root')
);