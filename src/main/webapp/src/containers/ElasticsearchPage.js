import React from "react"
import {connect} from "react-redux"
import Container from "react-bootstrap/Container"
import Row from "react-bootstrap/Row"
import Col from "react-bootstrap/Col"
import Button from "react-bootstrap/Button"
import Card from "react-bootstrap/Card"
import Table from "react-bootstrap/Table"
import Spinner from "react-bootstrap/Spinner"
import updateElasticsearchStatus from "../actions/updateElasticsearchStatus"
import resetElasticsearchSample from "../actions/resetElasticsearchSample"
import TopNavigationBar from "../components/TopNavigationBar"
import ErrorView from "../components/ErrorView"
import Alert from "react-bootstrap/Alert";

function mapStateToProps(state) {
    return {
        fetching: state.elasticsearchStatus.fetching,
        data: state.elasticsearchStatus.data,
        error: state.elasticsearchStatus.error,
        sampleReset: state.elasticsearchSampleReset,
    }
}

function mapDispatchToProps(dispatch) {
    return {
        updateElasticsearchStatus: () => dispatch(updateElasticsearchStatus()),
        resetElasticsearchSample: () => dispatch(resetElasticsearchSample())
    }
}

class ConnectedElasticsearchPage extends React.Component {
    constructor(props) {
        super(props)
    }

    componentDidMount() {
        this.props.updateElasticsearchStatus()
        this.updateElasticsearchStatusTimer = setInterval(() => this.props.updateElasticsearchStatus(), 15000)
    }

    componentWillUnmount() {
        clearInterval(this.updateElasticsearchStatusTimer)
    }

    handleResetSample = () => {
        this.props.resetElasticsearchSample()
    }

    render() {
        const esUrl = this.props.data && new URL(this.props.data.configuration.serviceUrl)
        const cluster = this.props.data && this.props.data.cluster
        const sampleReset = this.props.sampleReset

        return (
            <>
                <TopNavigationBar />
                <Container>
                    <Row className="py-3">
                        <Col>
                            <h1>Elasticsearch</h1>
                        </Col>
                        <Col sm="auto">
                            {this.props.fetching && <h2>&nbsp;<Spinner animation="border" /></h2>}
                        </Col>
                    </Row>
                    {this.props.error && <Row><Col><ErrorView error={this.props.error} suggestion="Make sure your Elasticsearch server is up and running at http://localhost:9200"/></Col></Row>}
                    <Row>
                        <Col lg={8}>
                            <Row>
                                {this.props.data && <Col md={6} className="pb-4">
                                    <Card>
                                        <Card.Header>Configuration</Card.Header>
                                        <Table striped>
                                            <tbody>
                                            <tr><td>Host</td><td>{esUrl.hostname}</td></tr>
                                            <tr><td>Post</td><td>{esUrl.port}</td></tr>
                                            <tr><td>Protocol</td><td>{esUrl.protocol.substr(0, esUrl.protocol.length - 1)}</td></tr>
                                            </tbody>
                                        </Table>
                                    </Card>
                                </Col>}
                                {cluster && <Col md={6} className="pb-4">
                                    <Card>
                                        <Card.Header>Cluster</Card.Header>
                                        <Table striped>
                                            <tbody>
                                            <tr><td>Name</td><td>{cluster.name}</td></tr>
                                            <tr><td>Status</td><td>{cluster.status}</td></tr>
                                            <tr><td>Documents</td><td>{cluster.numberOfDocuments}</td></tr>
                                            </tbody>
                                        </Table>
                                    </Card>
                                </Col>}
                            </Row>
                            <Row>
                                {cluster && <Col md={12} className="pb-4">
                                    <Card>
                                        <Card.Header>Indices</Card.Header>
                                        <Table striped>
                                            <thead>
                                            <tr>
                                                <th>Name</th>
                                                <th>Status</th>
                                                <th>Documents</th>
                                                <th>Size</th>
                                            </tr>
                                            </thead>
                                            <tbody>
                                            { cluster.indices.map(index => (
                                                <tr key={index.name}>
                                                    <td>{index.name}</td>
                                                    <td>{index.status}</td>
                                                    <td>{index.numberOfDocuments}</td>
                                                    <td>{index.size}</td>
                                                </tr>))}
                                            </tbody>
                                        </Table>
                                    </Card>
                                </Col>}
                            </Row>
                        </Col>
                        {cluster && <Col lg={4} className="pb-4">
                            <Card>
                                <Card.Header>Sample Data</Card.Header>
                                <Card.Body>
                                    <Card.Text>
                                        You are encouraged to create own indices and experiment with search.<br/>
                                        But it's also possible to (re)create index with sample data.
                                    </Card.Text>
                                    {sampleReset.error && <ErrorView error={sampleReset.error}/>}
                                    {!sampleReset.ok && <Button variant="primary" onClick={this.handleResetSample} disabled={sampleReset.posting}>Reset Sample Data</Button>}
                                    {sampleReset.ok && <Alert variant="success">
                                        <Alert.Heading>Oh Glory!</Alert.Heading>
                                        You have reset the sample index to it's initial state.
                                    </Alert>}
                                </Card.Body>
                            </Card>
                        </Col>}
                    </Row>
                    <br />
                </Container>
            </>
        )
    }
}

const ElasticsearchPage = connect(mapStateToProps, mapDispatchToProps)(ConnectedElasticsearchPage)

export default ElasticsearchPage