import types from '../actions/types'

const searchReducer = (state = {}, action) => {
    switch (action.type) {
        case types.SEARCH_STARTED: return { ...state, inProgress: true, results: null, error: null }
        case types.SEARCH_SUCCEEDED: return { ...state, inProgress: false, results: action.results }
        case types.SEARCH_FAILED: return { ...state, inProgress: false, error: action.error }
        default: return state
    }
}

export default searchReducer