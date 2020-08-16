import React from "react"
import Nav from "react-bootstrap/Nav"
import Navbar from "react-bootstrap/Navbar"

const TopNavigationBar = () => (
    <Navbar bg="dark" variant="dark">
        <Navbar.Brand href="/">Digger</Navbar.Brand>
        <Nav>
            <Nav.Link href="/about">About</Nav.Link>
        </Nav>
    </Navbar>
)

export default TopNavigationBar