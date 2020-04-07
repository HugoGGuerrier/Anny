// --- Resources import
import './css/common.css';

// --- Code imports
import React from 'react';
import ReactDOM from 'react-dom';
import AppRouter from './js/components/AppRouter'
import Config from "./js/tools/Config";

// Initialize the application configuration
Config.initConfig();

// Load the base component
ReactDOM.render(
  <AppRouter />,
  document.getElementById('root')
);