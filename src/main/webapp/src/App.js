import React from "react"
import {BrowserRouter as Router, Route} from "react-router-dom"
import SearchPage from "./containers/SearchPage"
import ElasticsearchPage from "./containers/ElasticsearchPage"
import AboutPage from "./containers/AboutPage"

const App = () => (
    <Router>
        <Route exact path="/" component={SearchPage} />
        <Route exact path="/elasticsearch" component={ElasticsearchPage} />
        <Route exact path="/about" component={AboutPage} />
    </Router>
)

export default App