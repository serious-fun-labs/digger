import request from 'axios'
import types from './types'

const updateElasticsearchStatus = () => async (dispatch) => {
    try {
        dispatch({ type: types.ELASTICSEARCH_STATUS_FETCHING })
        const response = await request.get('/api/elasticsearch/status')
        dispatch({ type: types.ELASTICSEARCH_STATUS_DATA, data: response.data })
    } catch (error) {
        dispatch({ type: types.ELASTICSEARCH_STATUS_ERROR, error: error })
    }
}

export default updateElasticsearchStatus