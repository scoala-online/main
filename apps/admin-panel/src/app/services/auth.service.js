import axios from 'axios';

axios.defaults.baseURL = process.env.NX_API_URL;

axios.interceptors.request.use(
  function (config) {
    const user = getUser();
    if (user && user.access_token) {
      axios.defaults.headers.common['Authorization'] = user.access_token;
    }
    return config;
  },
  function (error) {
    return Promise.reject(error);
  }
);

const isAuthenticated = () => {
  const user = getUser();

  if (user && user.access_token) {
    return true;
  }
  return false;
};

const login = async (username, password) => {
  const response = await axios.post('/users/login', {
    username,
    password,
  });
  if (response.data.access_token) {
    localStorage.setItem('user', JSON.stringify(response.data));
  }

  return response.data;
};

const logout = () => {
  localStorage.removeItem('user');
};

const getUser = () => {
  const user = localStorage.getItem('user');
  if (user) {
    return JSON.parse(user);
  }
  return null;
};

export default {
  login,
  logout,
  isAuthenticated,
  getUser,
};
