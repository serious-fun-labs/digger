import request from 'axios'
import types from './types'

const resetElasticsearchSample = () => async (dispatch) => {
    try {
        dispatch({ type: types.ELASTICSEARCH_SAMPLE_RESET_POSTING })
        await request.get('/api/elasticsearch/reset-sample')
        dispatch({ type: types.ELASTICSEARCH_SAMPLE_RESET_OK })
    } catch (error) {
        dispatch({ type: types.ELASTICSEARCH_SAMPLE_RESET_ERROR, error: error })
    }
}

export default resetElasticsearchSample