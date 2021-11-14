import Container from 'react-bootstrap/Container'
import Row from 'react-bootstrap/Row'
import Col from 'react-bootstrap/Col'

import Card from 'react-bootstrap/Card'

export default function InfoColumns(props) {
  // Props
  const cardItems = props.cardItems

  const cardStyle = {
    background: '#cccccc',
    width: '18vw',
    height: '40vh'
  }

  const rowStyle = {
    display: 'flex',
    justifyContent: 'center'
  }

  return (
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
              {cardItems.map(item => (
                <Col>
                  <Card className="text-center" style={cardStyle}>
                    <Card.Body>
                      <Card.Title style={{
                        fontSize: '20px'
                      }}>
                        {item.title}
                      </Card.Title>
                      <Card.Text>
                        {item.sub}
                      </Card.Text>
                    </Card.Body>
                  </Card>
                </Col>
              ))}
            </Row>
          </Container>
        </Row>
      </Col>
    </Container>
  )
}
