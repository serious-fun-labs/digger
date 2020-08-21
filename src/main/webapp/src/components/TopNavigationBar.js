import React from "react"
import Nav from "react-bootstrap/Nav"
import Navbar from "react-bootstrap/Navbar"
import NavDropdown from "react-bootstrap/NavDropdown"

const TopNavigationBar = () => (
    <Navbar bg="dark" variant="dark">
        <Navbar.Brand href="/">Digger</Navbar.Brand>
        <Nav>
            <Nav.Link href="/">Search</Nav.Link>
            <NavDropdown title="Develop">
                <NavDropdown.Item href="/elasticsearch">Elasticsearch</NavDropdown.Item>
            </NavDropdown>
            <Nav.Link href="/about">About</Nav.Link>
        </Nav>
    </Navbar>
)

export default TopNavigationBar