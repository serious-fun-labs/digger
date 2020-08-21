import React from "react"
import Alert from "react-bootstrap/Alert"

function ErrorView(props) {
    return <Alert variant="danger">
        <Alert.Heading>Oh Snap!</Alert.Heading>
        <p>{props.error.response.data}</p>
        {props.suggestion && <>
            <hr />
            <p>{props.suggestion}</p>
        </>}
    </Alert>
}

export default ErrorView