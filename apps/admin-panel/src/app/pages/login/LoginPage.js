import { useState } from 'react';
import { useHistory } from 'react-router-dom';
import { Container, Form, Button, Spinner } from 'react-bootstrap';

import AuthService from '../../services/auth.service';

import style from './LoginPageStyle';

export default function LoginPage(props) {
  const [isLoading, setIsLoading] = useState(false);
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [validated, setValidated] = useState(false);

  const history = useHistory();

  const onChangeUsername = (e) => {
    const formUsername = e.target.value;
    setUsername(formUsername);
  };

  const onChangePassword = (e) => {
    const formPassword = e.target.value;
    setPassword(formPassword);
  };

  const loginHandler = (e) => {
    const form = e.currentTarget;
    setIsLoading(true);
    //TODO: properly validate inputs
    if (form.checkValidity() === false) {
      e.preventDefault();
      e.stopPropagation();
    }

    AuthService.login(username, password).then(
      () => {
        history.push('/home');
      },
      (error) => {
        setIsLoading(false);
      }
    );
    setValidated(true);
  };

  return (
    <Container style={style.containerStyle}>
      <Form
        style={style.formStyle}
        validated={validated}
        // onSubmit={loginHandler}
      >
        <Form.Group
          className="mb-3"
          controlId="formBasicEmail"
          style={style.inputStyle}
        >
          <Form.Label>Email address:</Form.Label>
          <Form.Control
            required
            type="email"
            placeholder="user@example.com"
            onChange={onChangeUsername}
          />
        </Form.Group>
        <Form.Group
          className="mb-3"
          controlId="formBasicPassword"
          style={style.inputStyle}
        >
          <Form.Label>Password:</Form.Label>
          <Form.Control
            required
            type="password"
            placeholder="!Example123"
            onChange={onChangePassword}
          />
        </Form.Group>

        <Container style={style.buttonContainerStyle}>
          <Button
            variant="primary"
            // type="submit"
            style={style.buttonStyle}
            onClick={loginHandler}
          >
            {isLoading ? (
              <>
                <Spinner
                  as="span"
                  animation="grow"
                  variant="secondary"
                  size="sm"
                />{' '}
                Loading...
              </>
            ) : (
              <> Submit </>
            )}
          </Button>
        </Container>
      </Form>
    </Container>
  );
}
