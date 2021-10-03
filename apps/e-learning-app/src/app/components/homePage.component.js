import React, { Component } from 'react'

import Container from 'react-bootstrap/Container'
import Row from 'react-bootstrap/Row'
import Col from 'react-bootstrap/Col'

import Card from 'react-bootstrap/Card'
import Carousel from 'react-bootstrap/Carousel'

import { withRouter } from 'react-router-dom';

class HomePage extends Component {

  render() {
    const mainContanierStyle = {
      background: '#dddddd'
    }

    const titleStyle = {
      marginTop: '26vh',
      marginBottom: '39vh',
      marginLeft: '13vw',
      marginRight: '13vw'
    }

    const projectInfoStyle = {
      marginTop: '10vh',
      marginBottom: '19vh',
      marginLeft: '4vw',
      marginRight: '4vw'
    }

    const cardStyle = {
      background: '#cccccc',
      width: '18vw',
      height: '40vh'
    }

    const carouselContainerStyle = {
      marginTop: '14vh',
      marginBottom: '17vh',
      marginLeft: '7vw',
      marginRight: '7vw'
    }

    const carouselStyle = {
      width: '52vw',
      height: '51vh'
    }

    const carouselCardStyle = {
      background: '#446677',
      width: '52vw',
      height: '51vh'
    }

    return (
      <Container fluid style={mainContanierStyle}>
        <Row>
          <Container fluid style={titleStyle}>
            <Row>
              <h1>Scoala Online</h1>
            </Row>
            <Row>
              <h2>12 ani de educatie. Un singur site.</h2>
            </Row>
            <Row>
              <h3>
                Invata oriunde vrei, in ritmul tau. <br />
                Alege profesorul in functie de ce stil de explicatie preferi.
              </h3>
            </Row>
          </Container>
        </Row>
        <Row>
          <Col>
            <Container fluid style={projectInfoStyle}>
              <Col>
                <Row>
                  <div className='p-5 text-center'>
                    <h2 className='mb-3'>Ce ne dorim sa obtinem din acest proiect?</h2>
                  </div>
                </Row>
                <Row>
                  <Container fluid>
                    <Row>
                      <Col>
                        <Card className="text-center" style={cardStyle}>
                          <Card.Body>
                            <Card.Title>Invatamant gratuit</Card.Title>
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
          <Container fluid style={carouselContainerStyle}>
            <Row>
              <Carousel style={carouselStyle}>
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
