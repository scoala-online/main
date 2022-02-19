import { Card } from 'react-bootstrap';

/**
 * Renders a Card with a custom Title, Description, and link button.
 * @param {*} props
 * >
 * - `String` title
 * - `String` description
 * - `String` buttonText
 * - `String` buttonLink - default: `#`
 * - `Object` style - custom styling options for the card
 */
export default function CustomCard(props) {
  const title = props.title;
  const description = props.description;
  const buttonText = props.buttonText;
  const buttonLink = props.buttonLink || '#';
  const customStyle = props.style;

  return (
    <Card style={{ width: '100%', margin: '15px', ...customStyle }}>
      <Card.Body>
        <Card.Title>{title}</Card.Title>
        <Card.Text>{description}</Card.Text>
        <Card.Link href={buttonLink}>{buttonText}</Card.Link>
      </Card.Body>
    </Card>
  );
}
