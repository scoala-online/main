import Carousel from 'react-bootstrap/Carousel'

export default function CardCarousel(props) {
  // Props
  const pages = props.pages;

  const carouselCardStyle = {
    width: '52vw',
    height: '51vh'
  }

  // Parameters: quote, name, pictureURL
  return (
    <Carousel style={{
      width: '52vw',
      height: '51vh'
    }}>
      {pages.map(page => (
        <Carousel.Item>
          <div style={Object.assign(carouselCardStyle, {background: "#882277"})}>
            <h1>
              Hello 1
            </h1>
          </div>
          <Carousel.Caption>
            <h3>{page.quote}</h3>
            <p>{page.name}</p>
            <img src={page.pictureURL} />
          </Carousel.Caption>
        </Carousel.Item>
      ))}
    </Carousel>
  )
}
