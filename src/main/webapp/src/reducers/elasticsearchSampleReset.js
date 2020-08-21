import types from '../actions/types'

const elasticsearchSampleResetReducer = (state = {}, action) => {
    switch (action.type) {
        case types.ELASTICSEARCH_SAMPLE_RESET_POSTING: return { ...state, posting: true, ok: false, error: null }
        case types.ELASTICSEARCH_SAMPLE_RESET_OK: return { ...state, posting: false, ok: true }
        case types.ELASTICSEARCH_SAMPLE_RESET_ERROR: return { ...state, posting: false, error: action.error }
        default: return state
    }
}

export default elasticsearchSampleResetReducer