import React, { Component } from 'react'

import Container from 'react-bootstrap/Container'
import Row from 'react-bootstrap/Row'
import Col from 'react-bootstrap/Col'

import Card from 'react-bootstrap/Card'
import Carousel from 'react-bootstrap/Carousel'

import { withRouter } from 'react-router-dom';

class HomePage extends Component {

  render() {
    const cardStyle = {
      background: '#cccccc',
      width: '18vw',
      height: '40vh'
    }

    const carouselCardStyle = {
      background: '#446677',
      width: '52vw',
      height: '51vh'
    }

    const rowStyle = {
      display: 'flex',
      justifyContent: 'center'
    }

    return (
      <Container fluid style={{
        background: '#dddddd'
      }}>
        <Row>
          <Container fluid style={{
            marginTop: '26vh',
            marginBottom: '39vh',
            marginLeft: '13vw',
            marginRight: '13vw'
          }}>
            <Row style={rowStyle}>
              <span style={{
                fontWeight: 'bold',
                fontSize: '3.75rem',
                textAlign: 'center'
              }
              }>
                Scoala Online
              </span>
            </Row>
            <Row style={rowStyle}>
              <span style={{
                fontSize: '1.5rem',
                textAlign: 'center'
              }
              }>
                12 ani de educatie. Un singur site.
              </span>
            </Row>
            <Row style={ {...rowStyle, ...{
              margin: '30px'
            }}}>
              <span style={{
                fontSize: '1.875rem',
                textAlign: 'center'
              }}>
                Invata oriunde vrei, in ritmul tau.
              </span>
            </Row>
            <Row style={ {...rowStyle, ...{
              margin: '30px'
            }}}>
              <span style={{
                fontSize: '1.875rem',
                textAlign: 'center'
              }}>
                Alege profesorul in functie de ce stil de explicatie preferi.
              </span>
            </Row>
          </Container>
        </Row>
        <Row>
          <Col>
            <Container fluid style={{
              marginTop: '10vh',
              marginBottom: '19vh',
              marginLeft: '4vw',
              marginRight: '4vw'
            }}>
              <Col>
                <Row style={rowStyle}>
                  <span style={{
                    fontStyle: 'normal',
                    fontWeight: 600,
                    fontSize: '2rem',
                    display: 'flex',
                    alignItems: 'center',
                    textAlign: 'center'
                  }}>
                    Ce ne dorim sa obtinem din acest proiect?
                  </span>
                </Row>
                <Row>
                  <Container fluid>
                    <Row>
                      <Col>
                        <Card className="text-center" style={cardStyle}>
                          <Card.Body>
                            <Card.Title style={{
                              fontSize: '20px'
                            }}>
                              Invatamant gratuit
                            </Card.Title>
                            <Card.Text>
                              Intreaga materie scolara a fost filmata si oricine are acces nelimitat si gratuit la continut
                            </Card.Text>
                          </Card.Body>
                        </Card>
                      </Col>
                      <Col>
                        <Card className="text-center" style={cardStyle}>
                          <Card.Body>
                            <Card.Title>Invatamant de calitate</Card.Title>
                            <Card.Text>
                              Fiecare videoclip a fost verificat pentru a oferi informatii adevarate si cerem feedback regulat pentru a afla ce filmari trebuie refacute
                            </Card.Text>
                          </Card.Body>
                        </Card>
                      </Col>
                      <Col>
                        <Card className="text-center" style={cardStyle}>
                          <Card.Body>
                            <Card.Title>Invatamant accesibil</Card.Title>
                            <Card.Text>
                              Fiecare lectie este impartita in capitole si contine subtitrari, alaturi de suport de curs si transcript.
                            </Card.Text>
                          </Card.Body>
                        </Card>
                      </Col>
                    </Row>
                  </Container>
                </Row>
              </Col>
            </Container>
          </Col>
        </Row>
        <Row>
          <Container fluid style={{
            marginTop: '14vh',
            marginBottom: '17vh',
            marginLeft: '7vw',
            marginRight: '7vw'
          }}>
            <Row style={rowStyle}>
              <Carousel style={{
                width: '52vw',
                height: '51vh'
              }}>
                <Carousel.Item>
                  <div style={carouselCardStyle}>
                    <h1>
                      Hello 1
                    </h1>
                  </div>
                  <Carousel.Caption>
                    <h3>First slide label</h3>
                    <p>Nulla vitae elit libero, a pharetra augue mollis interdum.</p>
                  </Carousel.Caption>
                </Carousel.Item>
                <Carousel.Item>
                  <div style={carouselCardStyle}>
                    <h1>
                      Hello 2
                    </h1>
                  </div>

                  <Carousel.Caption>
                    <h3>Second slide label</h3>
                    <p>Lorem ipsum dolor sit amet, consectetur adipiscing elit.</p>
                  </Carousel.Caption>
                </Carousel.Item>
                <Carousel.Item>
                  <div style={carouselCardStyle}>
                    <h1>
                      Hello 3
                    </h1>
                  </div>

                  <Carousel.Caption>
                    <h3>Third slide label</h3>
                    <p>Praesent commodo cursus magna, vel scelerisque nisl consectetur.</p>
                  </Carousel.Caption>
                </Carousel.Item>
              </Carousel>
            </Row>
          </Container>
        </Row>
      </Container>
    );
  }
}

export default withRouter(HomePage);
