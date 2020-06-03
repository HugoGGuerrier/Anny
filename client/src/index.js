// --- Resources import
import './css/common.css';

// --- Code imports
import React from 'react';
import ReactDOM from 'react-dom';
import AppRouter from './js/components/AppRouter'
import StdVar from "./js/tools/StdVar";

// Initialize the application configuration
StdVar.initConfig();

// Load the base component
ReactDOM.render(
  <AppRouter />,
  document.getElementById('root')
);