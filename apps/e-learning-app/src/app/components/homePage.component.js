import React, { Component } from 'react'
import Container from 'react-bootstrap/Container'
import Row from 'react-bootstrap/Row'
import Col from 'react-bootstrap/Col'
import Carousel from 'react-bootstrap/Carousel'

import { withRouter } from 'react-router-dom';

class HomePage extends Component {

  render() {
    const mainContanierStyle = {
      height: '5px'
    }

    return (
      <Container style={mainContanierStyle}>
        <Row>
          <Col />
          <Col>
            <Container>
              <Col>
                <Row>Header and Title</Row>
                <Row>
                  <h2>Ce ne dorim</h2>
                  <Container>
                    <Row>
                      <Col>1</Col>
                      <Col>2</Col>
                      <Col>3</Col>
                    </Row>
                  </Container>
                </Row>
                <Row>
                  <Carousel>

                  </Carousel>
                </Row>
              </Col>
            </Container>
          </Col>
          <Col />
        </Row>
      </Container>
    );
  }
}

export default withRouter(HomePage);
