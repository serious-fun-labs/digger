import React from "react"
import Card from "react-bootstrap/Card"

function ResultsView(props) {
    return <Card>
        <Card.Header>Results</Card.Header>
        <Card.Body>
            <Card.Text>{props.results}</Card.Text>
            <Card.Link href="/about">About project</Card.Link>
        </Card.Body>
    </Card>
}

export default ResultsView