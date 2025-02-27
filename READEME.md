### react 생성
npx create-react-app [생성할 이름] --template typescript
ex) npx create-react-app front --template typescript 타입스크림을 사용할거면 추가

### tsconfig.json 에 "baseUrl" : "./src" 을 추가하면 경로 작성할때 매우 편해진다 

// 예시: baseUrl이 설정되지 않은 경우
import myModule from '../../../myModule';

// 예시: baseUrl이 './src'로 설정된 경우
import myModule from 'myModule'