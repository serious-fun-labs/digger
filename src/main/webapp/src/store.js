import { applyMiddleware, compose, createStore } from 'redux'
import thunk from "redux-thunk";
import { createLogger } from "redux-logger";
import rootReducer from "./reducers";

const enhancers = []
const middleware = [thunk, createLogger()]

if (process.env.NODE_ENV === 'development') {
    const devToolsExtension = window.__REDUX_DEVTOOLS_EXTENSION__

    if (typeof devToolsExtension === 'function') {
        enhancers.push(devToolsExtension())
    }
}

const composedEnhancers = compose(
    applyMiddleware(...middleware),
    ...enhancers
)

export default createStore(
    rootReducer,
    {},
    composedEnhancers
)
