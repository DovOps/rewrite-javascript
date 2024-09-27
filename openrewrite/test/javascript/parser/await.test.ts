import {connect, disconnect, rewriteRun, typeScript} from '../testHarness';

describe('await mapping', () => {
    beforeAll(() => connect());
    afterAll(() => disconnect());

    test('simple', () => {
        rewriteRun(
          //language=typescript
          typeScript('await 1')
        );
    });
});
