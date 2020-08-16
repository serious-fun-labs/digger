import './style.scss'
import 'babel-polyfill'

import React from 'react'
import ReactDOM from 'react-dom'
import { Provider } from 'react-redux'

import store from './store'
import App from './App'

ReactDOM.render(
    <Provider store={store}>
        <App history={history} />
    </Provider>,
    document.getElementById('container'))