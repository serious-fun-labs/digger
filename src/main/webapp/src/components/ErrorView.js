import React from "react"
import Alert from "react-bootstrap/Alert"

function ErrorView(props) {
    return <Alert variant="danger">
        <Alert.Heading>Oh snap!</Alert.Heading>
        {props.error.response.data}
    </Alert>
}

export default ErrorView