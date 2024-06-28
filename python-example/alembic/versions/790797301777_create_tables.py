"""create tables

Revision ID: 790797301777
Revises: 
Create Date: 2024-06-29 05:28:31.587123

"""

from typing import Sequence, Union

import sqlalchemy as sa

from alembic import op

# revision identifiers, used by Alembic.
revision: str = "790797301777"
down_revision: Union[str, None] = None
branch_labels: Union[str, Sequence[str], None] = None
depends_on: Union[str, Sequence[str], None] = None


def upgrade() -> None:
    op.create_table(
        "comics",
        sa.Column("id", sa.Integer, primary_key=True),
        sa.Column("title", sa.String(100), nullable=False),
        sa.Column("author", sa.String(100), nullable=False),
    )


def downgrade() -> None:
    op.drop_table("comics")
