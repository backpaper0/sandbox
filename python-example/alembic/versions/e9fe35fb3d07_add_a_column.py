"""Add a column

Revision ID: e9fe35fb3d07
Revises: 790797301777
Create Date: 2024-06-29 05:35:38.358346

"""

from typing import Sequence, Union

import sqlalchemy as sa

from alembic import op

# revision identifiers, used by Alembic.
revision: str = "e9fe35fb3d07"
down_revision: Union[str, None] = "790797301777"
branch_labels: Union[str, Sequence[str], None] = None
depends_on: Union[str, Sequence[str], None] = None


def upgrade() -> None:
    op.add_column("comics", sa.Column("publisher", sa.String(100), nullable=False))


def downgrade() -> None:
    op.drop_column("comics", "publisher")
