import styled from 'styled-components';

/**
 * Container for the content of a sidebar
 */
export const SidebarContainer = styled.div`
  background: ${({ type }) =>
    type === 'grade'
      ? 'linear-gradient(180deg, rgba(0, 201, 183, 0.9) 0%, rgba(0, 224, 206, 0.9) 100%)'
      : 'linear-gradient(319.97deg, #009C8E 7.3%, #00E0CE 100%)'};
  border-radius: 0px;

  display: flex;
  flex-direction: column;
  font-size: 1.5rem;

  position: fixed;
  width: ${({ type }) => (type === 'grade' ? '15.8vw' : '12.2vw')};
  bottom: ${({ bottomVal }) => bottomVal};
  top: ${({ topVal }) => topVal};
  left: ${({ active, type }) => {
    if (type === 'grade') {
      if (active) {
        return '0';
      }
      return '-15.8vw';
    }

    if (active) {
      return '15.8vw';
    }
    return '-28vw';
  }};
  z-index: ${({ type }) => (type === 'grade' ? '100' : '99')};

  transition: left ${({ type }) => (type === 'grade' ? '350ms' : '1000ms')};

  direction: ${({ type }) => (type === 'grade' ? 'rtl' : 'ltr')};
  overflow-y: auto;
  overflow-x: hidden;
`;
