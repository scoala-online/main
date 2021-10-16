import styled from 'styled-components';

import { sidebarButtonHeight } from './Sizes';

/**
 * Button inside a sidebar used to open another sidebar or another page.
 */
export const SidebarButton = styled.button`
  background: transparent;
  min-height: ${sidebarButtonHeight}vh;
  width: ${({ type }) => (type === 'grade' ? '15.8vw' : '12.2vw')};

  display: flex;
  justify-content: center;
  align-items: center;

  border-right: 1px solid #fffdf6;
  border-left: 1px solid #fffdf6;
  border-top: 0;
  border-bottom: ${({ last }) => (last ? '0' : '1px solid #fffdf6')};

  box-sizing: border-box;
`;
