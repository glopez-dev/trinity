import {describe, expect, it} from 'vitest';
import {render, screen} from '@testing-library/react';
import Badge from "@/components/ui/badge/Badge";

describe('Badges', () => {

    it('render correctly with default props' , () => {
        render(<Badge text={'Badge'} type={'success'} data-testid={'badge-success'}/>);
        const badge = screen.getByRole('badge');
        expect(badge).toBeDefined();
    })

    it('applies different variants correctly', () => {
        const {rerender} = render(<Badge text={'Badge Success'} type={'success'}/>);
        expect(screen.getByRole('badge').getAttribute('class')).toContain('success');

        rerender(<Badge text={'Badge Warning'} type={'warning'}/>);
        expect(screen.getByRole('badge').getAttribute('class')).toContain('warning');

        rerender(<Badge text={'Badge Error'} type={'error'}/>);
        expect(screen.getByRole('badge').getAttribute('class')).toContain('error');

        rerender(<Badge text={'Badge Info'} type={'info'}/>);
        expect(screen.getByRole('badge').getAttribute('class')).toContain('info');
    });

});