import React from "react"
import Container from "react-bootstrap/Container"
import Row from "react-bootstrap/Row"
import Col from "react-bootstrap/Col"
import Card from "react-bootstrap/Card"
import TopNavigationBar from "../components/TopNavigationBar"

function AboutPage() {
    return <>
        <TopNavigationBar />
        <Container>
            <Row>
                <Col>
                    <h1 className="py-3">About</h1>
                </Col>
            </Row>
            <Row>
                <Col md={8} lg={9}>
                    <Card body>
                        <Card.Title>Codename Digger</Card.Title>
                        <Card.Text>
                            The Ultimate DevOps' tool to squeeze every last bit of information from logs.
                        </Card.Text>
                    </Card>
                </Col>
                <Col md={4} lg={3}>
                    <Card>
                        <Card.Header>Links</Card.Header>
                        <Card.Body>
                            <Card.Link href="https://github.com/serious-fun-labs/digger">Sources</Card.Link><br/>
                            <Card.Link href="https://github.com/serious-fun-labs/digger-specs">Specs</Card.Link><br/>
                            <Card.Link href="https://github.com/orgs/serious-fun-labs/projects/1">Project</Card.Link>
                        </Card.Body>
                    </Card>
                </Col>
            </Row>
        </Container>
    </>
}

export default AboutPage