import styled from 'styled-components';

//Button inside a sidebar used to open another sidebar or another page
export const LinkButton = styled.button`
  background: transparent;
  min-height: 6.85vh;
  width: ${({ type }) => (type === 'grade' ? '15.8vw' : '12.2vw')};

  display: flex;
  justify-content: center;
  align-items: center;

  border-top: 0px solid #fffdf6;
  border-right: 1px solid #fffdf6;
  border-left: 1px solid #fffdf6;
  border-bottom: ${({ type, last }) =>
      last ? '0' : type === 'grade' ? '1px' : '1px'}
    solid #fffdf6;

  box-sizing: border-box;
`;
