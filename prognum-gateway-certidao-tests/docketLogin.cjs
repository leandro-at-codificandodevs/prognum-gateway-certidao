const login = 'g.parcero@prognum.com.br';
const senha = 'Prognum@2024';

const docketLogin = async () => {
  const response = await fetch('https://sandbox-saas.docket.com.br/api/v2/auth/login', {
    method: 'POST',
    headers: {
      'Accept': '*/*',
      'Content-Type': 'application/json',
    },
    body: JSON.stringify({ login, senha }),
  });

  const data = await response.json();
  console.log(data);
}

export default docketLogin;
