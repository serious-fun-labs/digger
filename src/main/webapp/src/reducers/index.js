import { combineReducers } from 'redux'

import searchReducer from "./search"
import elasticsearchStatusReducer from "./elasticsearchStatus"
import elasticsearchSampleResetReducer from "./elasticsearchSampleReset"

const rootReducer = combineReducers({
    search: searchReducer,
    elasticsearchStatus: elasticsearchStatusReducer,
    elasticsearchSampleReset: elasticsearchSampleResetReducer
})

export default rootReducer