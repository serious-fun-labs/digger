import types from '../actions/types'

const elasticsearchStatusReducer = (state = {}, action) => {
    switch (action.type) {
        case types.ELASTICSEARCH_STATUS_FETCHING: return { ...state, fetching: true, error: null }
        case types.ELASTICSEARCH_STATUS_DATA: return { ...state, fetching: false, data: action.data }
        case types.ELASTICSEARCH_STATUS_ERROR: return { ...state, fetching: false, error: action.error }
        default: return state
    }
}

export default elasticsearchStatusReducer