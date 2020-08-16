import React from "react"
import { connect } from "react-redux"
import Button from "react-bootstrap/Button"
import Form from "react-bootstrap/Form"
import InputGroup from "react-bootstrap/InputGroup"
import Spinner from "react-bootstrap/Spinner"
import search from "../actions/search"

function mapStateToProps(state) {
    return {
        inProgress: state.search.inProgress
    }
}

function mapDispatchToProps(dispatch) {
    return {
        search: text => dispatch(search(text))
    }
}

class ConnectedSearchBox extends React.Component {
    constructor(props) {
        super(props)

        this.state = { query: "" }
    }

    handleSubmit = (event) => {
        event.preventDefault()

        this.props.search(this.state.query)
    }

    handleQueryChange = (event) => {
        this.setState({ query: event.target.value })
    }

    render() {
        return (
            <Form onSubmit={this.handleSubmit}>
                <InputGroup>
                    <Form.Control id="query" placeholder="Enter search terms..." value={this.state.query} onChange={this.handleQueryChange} />
                    <InputGroup.Append>
                        <Button type="submit" variant="outline-primary" disabled={this.props.inProgress}>
                            {(() => {
                                if (this.props.inProgress) {
                                    return <Spinner as="span" animation="border" size="sm" role="status" aria-hidden="true"/>
                                } else {
                                    return <i className="fa fa-search" />
                                }
                            })()}
                        </Button>
                    </InputGroup.Append>
                </InputGroup>
            </Form>
        )
    }
}

const SearchBox = connect(mapStateToProps, mapDispatchToProps)(ConnectedSearchBox)

export default SearchBox