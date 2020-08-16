import React from "react"
import { useSelector, useDispatch } from "react-redux"
import Container from "react-bootstrap/Container"
import Row from "react-bootstrap/Row"
import Col from "react-bootstrap/Col"
import TopNavigationBar from "../components/TopNavigationBar"
import SearchBox from "../components/SearchBox"
import ResultsView from "../components/ResultsView"
import ErrorView from "../components/ErrorView"

function SearchPage() {
    const error = useSelector(s => s.search.error)
    const results = useSelector(s => s.search.results)

    return <>
        <TopNavigationBar />
        <Container>
            <Row>
                <Col>
                    <h1 className="py-3">Search</h1>
                </Col>
            </Row>
            <Row>
                <Col>
                    <SearchBox />
                </Col>
            </Row>
            <br />
            { error && <Row><Col><ErrorView error={error}/></Col></Row> }
            { results && <Row><Col><ResultsView results={results} /></Col></Row> }
        </Container>
    </>
}

export default SearchPage