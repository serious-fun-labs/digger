import request from 'axios'
import types from './types'

const search = (query) => async (dispatch) => {
    try {
        dispatch({ type: types.SEARCH_STARTED })
        const response = await request.get('/api/search', { params: { query: query }})
        dispatch({ type: types.SEARCH_SUCCEEDED, results: response.data })
    } catch (error) {
        dispatch({ type: types.SEARCH_FAILED, error: error })
    }
}

export default search